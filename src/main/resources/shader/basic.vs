#version 330 core

uniform bool transforms;
uniform mat4 transformation;

layout (location = 0) in vec3 vertPos;

void main() {
    mat4 transform;
    if (transforms) {
        transform = transformation;
    } else {
        transform = mat4(1.0);
    }
    
	gl_Position = transform * vec4(vertPos, 1.0);
}
