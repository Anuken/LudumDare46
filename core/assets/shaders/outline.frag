#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying LOWP vec4 v_mix_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform vec2 u_step;
uniform vec4 u_outline;

bool exists(vec2 offset){
    return texture2D(u_texture, v_texCoords + offset*u_step).a > 0.01;
}

void main(){
    vec4 c = texture2D(u_texture, v_texCoords);

    if(c.a < 0.01){
        if(exists(vec2(1.0, 0.0)) || exists(vec2(-1.0, 0.0)) || exists(vec2(0.0, 1.0)) || exists(vec2(0.0, -1.0))){
            gl_FragColor = u_outline;
        }
    }else{
        gl_FragColor = v_color * mix(c, vec4(v_mix_color.rgb, c.a), v_mix_color.a);
    }
}