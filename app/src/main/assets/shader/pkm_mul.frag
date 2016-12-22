precision mediump float;
varying vec2 aCoord;
uniform sampler2D vTexture;
uniform sampler2D vTextureAlpha;

void main() {
    vec4 color=texture2D( vTexture, aCoord);
    color.a=texture2D(vTextureAlpha,aCoord).r;
    gl_FragColor = color;
}