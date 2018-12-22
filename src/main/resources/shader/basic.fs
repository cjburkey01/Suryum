#version 330 core

uniform vec3 albedo;

out vec4 fragColor;

void main() {
	fragColor = vec4(albedo, 1.0);
}
