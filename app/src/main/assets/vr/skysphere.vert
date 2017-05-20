uniform mat4 uMatrix;

attribute vec3 aPosition;
attribute vec2 aCoordinate;

varying vec2 vCoordinate;

void main(){
    gl_Position=uMatrix*vec4(aPosition,1);
    vCoordinate=aCoordinate;
}