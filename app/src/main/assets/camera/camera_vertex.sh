attribute vec4 vPosition;
uniform mat4 vMatrix;
attribute vec2 inputTextureCoordinate;
varying vec2 textureCoordinate;

void main(){
    gl_Position = vMatrix*vPosition;
    textureCoordinate = inputTextureCoordinate;
}