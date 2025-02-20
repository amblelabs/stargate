#version 150

uniform float iTime;
in vec2 texCoord;

out vec4 fragColor;

float ang(vec2 uv, vec2 center) {
    return atan((uv.y - center.y), (uv.x - center.x));
}

float spir(vec2 uv, vec2 loc) {
    float dist1 = length(uv - loc);
    float dist2 = dist1 * dist1;
    float layer6 = sin((ang(uv, loc) + dist2 - iTime) * 6.0);
    layer6 = layer6 * dist1;
    return layer6;
}

float height(vec2 uvCoord) {
    float layer1 = sin(iTime * 8.54 -sin(length(uvCoord - vec2(-0.41, -0.47))) * 55.0);
    float layer2 = sin(iTime * 7.13 -sin(length(uvCoord - vec2(1.35, 1.32))) * 43.0);
    float layer3 = sin(iTime * 7.92 -sin(length(uvCoord - vec2(-0.34, 1.28))) * 42.5);
    float layer4 = sin(iTime * 6.71 -sin(length(uvCoord - vec2(1.23, -0.24))) * 47.2);

    float spiral = spir(uvCoord, vec2(0.5, 0.5));
    spiral *= 3.0;

    float temp = layer1 + layer2 + layer3 + layer4 + spiral;

    float b = smoothstep(-2.5, 5.0, temp);
    return b * 3.0;
}

void main() {
    vec2 uv = texCoord.xy / iResolution.xy;
    uv.x -= .25;
    uv.x *= iResolution.x / iResolution.y;

    if (length(uv - vec2(0.5, 0.5)) > 0.5) {
        fragColor = vec4(0.0, 0.0, 0.0, 0.0);
        return;
    }

    float waveHeight = (height(uv) * .4) + (.2 / length(uv - (sin (iTime) * .5 + .5) +
    vec2(height (uv + vec2(.01, .0)) - height(uv + vec2(-.01, 0.)), height(uv + vec2(.0, .01)) - height(uv + vec2(0., -.01)))));

    vec3 color = vec3(waveHeight * 0.3, waveHeight * 0.5, waveHeight);

    fragColor = vec4(color, 1.0);
}

