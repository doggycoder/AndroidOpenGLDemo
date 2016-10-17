precision mediump float;

uniform sampler2D vTexture;

varying vec2 aCoordinate;

void main(){
    vec3 mColor=texture2D(vTexture,aCoordinate).rgb;
    gl_FragColor=vec4(mColor,1.0);
}