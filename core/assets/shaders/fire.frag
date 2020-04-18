#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

#define PALETTE_SIZE 3

varying LOWP vec4 v_color;
varying LOWP vec4 v_mix_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_time;
uniform vec3 palette[PALETTE_SIZE];

const float disc = 0.3;
const float thickness = 5.0 / 3.0;
const float height = 3.0;

vec2 hash(vec2 p){
    p = vec2(dot(p, vec2(127.1, 311.7)),
    dot(p, vec2(269.5, 183.3)));
    return -1.0 + 2.0*fract(sin(p)*43758.5453123);
}

float noise(in vec2 p){
    const float K1 = 0.366025404;
    const float K2 = 0.211324865;

    vec2 i = floor(p + (p.x+p.y)*K1);

    vec2 a = p - i + (i.x+i.y)*K2;
    vec2 o = (a.x>a.y) ? vec2(1.0, 0.0) : vec2(0.0, 1.0);
    vec2 b = a - o + K2;
    vec2 c = a - 1.0 + 2.0*K2;

    vec3 h = max(0.5-vec3(dot(a, a), dot(b, b), dot(c, c)), 0.0);

    vec3 n = h*h*h*h*vec3(dot(a, hash(i+0.0)), dot(b, hash(i+o)), dot(c, hash(i+1.0)));

    return dot(n, vec3(70.0));
}

float fbm(vec2 uv){
    float f;
    mat2 m = mat2(1.6, 1.2, -1.2, 1.6);
    f  = 0.5000*noise(uv); uv = m*uv;
    f += 0.2500*noise(uv); uv = m*uv;
    //f += 0.1250*noise( uv ); uv = m*uv;
    f += 0.0625*noise(uv); uv = m*uv;
    f = 0.5 + 0.5*f;
    return f;
}

void main(){
    float time = u_time / 2.0;
    vec2 uv = v_texCoords;
    vec2 q = uv;
    q.x *= thickness;
    q.y *= height;
    float strength = 2.0;
    float T3 = max(3., 1.25*strength)*time;
    q.x -= thickness/2.0;
    q.y -= 0.25;
    float n = fbm(strength*q - vec2(0, T3));
    float c = 1. - 16. * pow(max(0., length(q*vec2(1.8+q.y*1.5, .75)) - n * max(0., q.y+.25)), 1.2);
    float c1 = n * c * (1.5-pow(2.50*uv.y, 4.));
    c1=clamp(c1, 0., 1.);

    vec3 col;

    if(c1 < 0.33) col = palette[0];
    else if(c1 < 0.66) col = palette[1];
    else col = palette[2];

    float a = c * (1.-pow(uv.y, 3.));

    a = 1.0-step(a, 0.5);

    gl_FragColor = vec4(col, a);
}