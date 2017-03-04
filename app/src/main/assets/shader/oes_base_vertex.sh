attribute vec4 vPosition;
attribute vec2 vCoord;
uniform mat4 vMatrix;
uniform mat4 vCoordMatrix;
varying vec2 textureCoordinate;

void main(){
    gl_Position = vMatrix*vPosition;
    textureCoordinate = (vCoordMatrix*vec4(vCoord,0,1)).xy;
}