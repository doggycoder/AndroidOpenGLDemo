uniform mat4 vMatrix;
varying vec4 vColor;
attribute vec4 vPosition;

void main(){
    gl_Position=vMatrix*vPosition;
    float color;
    if(vPosition.z>0.0){
        color=vPosition.z;
    }else{
        color=-vPosition.z;
    }
    vColor=vec4(color,color,color,1.0);
}