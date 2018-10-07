package cz.pstechmu.apg.engine;

import cz.pstechmu.apg.util.BufferUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

public abstract class Shader {

    private static final String VERTEX_EXTENTION = "vs";
    private static final String FRAGMENT_EXTENTION = "fs";

    private static final String PATH_FORMAT = "/shaders/%s.%s";

    private int pid;
    private HashMap<String, Integer> uniforms;

    /**
     * Creates a shaders with the specified file name.
     * @param name The filename of the shaders as found in the specified PATH with the specified extensions
     */
    public Shader(String name) throws IOException {
        // Create the program id
        pid = GL20.glCreateProgram();

        // Instantiate the uniform lookup
        uniforms = new HashMap<>();

        // Add the vertex shaders to the program
        String vertSource = addShader(GL20.GL_VERTEX_SHADER, String.format(PATH_FORMAT, name, VERTEX_EXTENTION));
        // Add the fragment shaders to the program
        String fragSource = addShader(GL20.GL_FRAGMENT_SHADER, String.format(PATH_FORMAT, name, FRAGMENT_EXTENTION));

        // Link the program
        GL20.glLinkProgram(pid);
        // Check if linking was successful
        closeIfProgramError(GL20.glGetProgrami(pid, GL20.GL_LINK_STATUS) == 0, pid);

        // Validate the program
        GL20.glValidateProgram(pid);
        // Check if validation was successful
        closeIfProgramError(GL20.glGetProgrami(pid, GL20.GL_VALIDATE_STATUS) == 0, pid);

        // Bind the shaders
        bind();

        // Automatically add uniforms
        autoAddUniforms(vertSource);
        autoAddUniforms(fragSource);

        bindAttributes();
    }

    /**
     * Adds the specified shaders part to the shaders program
     * @param type The type of shaders to add
     * @param fileName The full path of the shaders
     * @return The shaders source
     */
    private String addShader(int type, String fileName) throws IOException {
        // The loaded shaders source
        String shaderSource;

        // Load the file
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));

        // Read the file one line at a time
        String line;
        while((line = reader.readLine()) != null) {
            // Add the line to the source
            builder.append(line).append("\n");
        }

        // Release memory resources
        reader.close();

        // Set the shaders source to the loaded file
        shaderSource = builder.toString();

        // The id of the shaders part
        int id = GL20.glCreateShader(type);

        // Set the source of the shaders
        GL20.glShaderSource(id, shaderSource);
        // Compile the shaders source
        GL20.glCompileShader(id);

        // Check to make sure compliation was successful
        closeIfShaderError(GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == 0, id);

        // Attach the shaders to the program
        GL20.glAttachShader(pid, id);

        return shaderSource;
    }

    // Contains all the standard GLSL variable types to compare to custom types
    private static final Set<String> TYPE_KEYWORDS = new HashSet<>();
    private static final String[] KEYWORDS_ARRAY = new String[] {
        "bool", "int", "uint", "float", "double", "bvec2", "bvec3", "bvec4",
        "ivec2", "ivec3", "ivec4", "uvec2", "uvec3", "uvec4", "vec2", "vec3",
        "vec4", "dvec2", "dvec3", "dvec4", "mat2", "mat3", "mat4", "dmat2",
        "dmat3", "dmat4", "mat2x2", "mat2x3", "mat2x4", "dmat2x2", "dmat2x3",
        "dmat2x4", "mat3x2", "mat3x3", "mat3x4", "dmat3x2", "dmat3x3", "dmat3x4",
        "mat4x2", "mat4x3", "mat4x4", "dmat4x2", "dmat4x3", "dmat4x4",
        "sampler1D", "sampler1D", "sampler2D", "sampler3D", "samplerCube",
        "sampler1DShadow", "sampler2DShadow", "samplerCubeShadow",
        "sampler1DArray", "sampler2DArray",
        "sampler1DArrayShadow", "sampler2DArrayShadow",
        "isampler1D", "isampler2D", "isampler3D", "isamplerCube",
        "isampler1DArray", "isampler2DArray",
        "usampler1D", "usampler2D", "usampler3D", "usamplerCube",
        "usampler1DArray", "usampler2DArray",
        "sampler2DRect", "sampler2DRectShadow", "isampler2DRect", "usampler2DRect",
        "samplerBuffer", "isamplerBuffer", "usamplerBuffer",
        "sampler2DMS", "isampler2DMS", "usampler2DMS",
        "sampler2DMSArray", "isampler2DMSArray", "usampler2DMSArray",
        "samplerCubeArray", "samplerCubeArrayShadow", "isamplerCubeArray", "usamplerCubeArray"
    };

    /**
     * Fills the TYPE_KEYWORDS Set with all the GLSL type keywords for easy lookup
     */
    static {
        TYPE_KEYWORDS.addAll(Arrays.asList(KEYWORDS_ARRAY));
    }

    /**
     * Automatically detects and adds uniforms from the shaders source
     * @param shaderSource
     */
    private void autoAddUniforms(String shaderSource) {
        // The uniform keyword to detect uniforms
        final String UNIFORM_KEYWORD = "uniform";

        // Holds the start location of the current uniform
        int uniformLocation = -1;

        // Iterates through the shaders source looking for the uniform keyword
        while((uniformLocation = shaderSource.indexOf(UNIFORM_KEYWORD, ++uniformLocation)) != -1) {
            // Holds the location of the end of the line
            int newLineLocation = shaderSource.indexOf("\n", uniformLocation);
            // The line that the uniform is declared on
            String line = shaderSource.substring(uniformLocation, newLineLocation).trim();
            // The different tokens (words) of the line
            String[] tokens = line.split(" ");

            // Holds the name of the uniform
            String name = tokens[2].substring(0, tokens[2].length() - 1).trim();
            // Holds the type of the uniform
            String type = tokens[1].trim();

            // Adds the uniform to the shaders program and HashMap lookup
            if(TYPE_KEYWORDS.contains(type)) {
                addUniform(name);
            } else {
                addCustomUniform(type, name);
            }
        }
    }

    /**
     * Method to be overridden by child class to add custom uniforms like structs
     * @param type The type of the uniform
     * @param name The name of the uniform
     */
    protected void addCustomUniform(String type, String name) {}

    /**
     * Closes the application and prints an info log if an error occurs
     * @param error Whether or not there is an error
     * @param id The id of the program
     */
    private void closeIfProgramError(boolean error, int id) {
        if(error) {
            // Print the error log of the program with a max length of 1024 bytes
            System.err.println(GL20.glGetProgramInfoLog(id, 1024));
            System.exit(1);
        }
    }

    /**
     * Closes the application and prints an info log if an error occurs
     * @param error Whether or not there is an error
     * @param id The id of the shaders
     */
    private void closeIfShaderError(boolean error, int id) {
        if(error) {
            // Print the error log of the shaders with a max length of 1024 bytes
            System.err.println(GL20.glGetShaderInfoLog(id, 1024));
            System.exit(1);
        }
    }

    public abstract void update(Camera camera);

    /**
     * Binds this shaders
     */
    public void bind() {
        GL20.glUseProgram(pid);
    }

    /**
     * Unbinds this shaders
     */
    public void unbind() {
        GL20.glUseProgram(0);
    }

    protected abstract void bindAttributes();

    /**
     * Binds an attribute to this shaders
     * @param index The index of the attribute
     * @param name The name of the attribute
     */
    public void bindAttribute(int index, String name) {
        GL20.glBindAttribLocation(pid, index, name);
    }

    /**
     * Adds the specified uniform to the lookup table and shaders
     * @param uniform
     */
    public void addUniform(String uniform) {
        // The location in memory of the uniform
        int location = GL20.glGetUniformLocation(pid, uniform);

        // If the uniform does not exist
        if(location == -1) {
            System.err.println("Error in Shader.addUniform(): uniform [" + uniform + "] does not exist");
            System.exit(1);
        }

        // Add the uniform to the lookup table
        uniforms.put(uniform, location);
    }

    /**
     * Sets the specified uniform with the specified value
     * @param uniform
     * @param value The integer value
     */
    public void setUniform(String uniform, int value) {
        GL20.glUniform1i(uniforms.get(uniform), value);
    }

    /**
     * Sets the specified uniform with the specified value
     * @param uniform
     * @param value The float value
     */
    public void setUniform(String uniform, float value) {
        GL20.glUniform1f(uniforms.get(uniform), value);
    }

    /**
     * Sets the specified uniform with the specified value
     * @param uniform
     * @param value The Vector3f value
     */
    public void setUniform(String uniform, Vector3f value) {
        GL20.glUniform3f(uniforms.get(uniform), value.x, value.y, value.z);
    }

    /**
     * Sets the specified uniform with the specified value
     * @param uniform
     * @param value The Vector4f value
     */
    public void setUniform(String uniform, Vector4f value) {
        GL20.glUniform4f(uniforms.get(uniform), value.x, value.y, value.z, value.w);
    }

    /**
     * Sets the specified uniform with the specified value
     * @param uniform
     * @param value The Matrix4f value
     */
    public void setUniform(String uniform, Matrix4f value) {
        GL20.glUniformMatrix4fv(uniforms.get(uniform), false, BufferUtil.toBuffer(value));
    }

}
