uniform mat4 vMatrix;

attribute vec4 vPosition;
attribute vec2 aCoord;
varying vec2 vCoord;
void main(){
    gl_Position = vPosition;
    vCoord = aCoord;
}