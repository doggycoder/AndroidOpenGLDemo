precision highp float;

uniform sampler2D uTexture;
varying vec2 vCoordinate;

void main(){
   vec4 color=texture2D(uTexture,vCoordinate);
   gl_FragColor=color;
}