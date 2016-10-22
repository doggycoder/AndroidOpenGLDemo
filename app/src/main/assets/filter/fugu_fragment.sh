precision mediump float;

uniform sampler2D vTexture;

varying vec2 aCoordinate;

void main(){
    vec4 nColor=texture2D(vTexture,aCoordinate);
    float c=nColor.r*0.299+nColor.g*0.587+nColor.b*0.114;
    gl_FragColor=vec4(c,c,c,nColor.a);
}