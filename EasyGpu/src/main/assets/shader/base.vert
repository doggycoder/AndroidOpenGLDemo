attribute vec4 aPosition;
attribute vec2 aCoord;
uniform mat4 uMatrix;

varying vec2 vCoord;

void main(){
    gl_Position = uMatrix*aPosition;
    aCoord = aCoord;
}