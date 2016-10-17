attribute vec4 vPosition;
attribute vec2 vCoordinate;

varying vec2 aCoordinate;

void main(){
    gl_Position=vPosition;
    aCoordinate=vCoordinate;
}