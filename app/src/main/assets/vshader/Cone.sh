uniform mat4 vMatrix;
varying vec4 vColor;
attribute vec4 vPosition;

void main(){
    gl_Position=vMatrix*vPosition;
    if(vPosition.z!=0.0){
        vColor=vec4(0.0,0.0,0.0,1.0);
    }else{
        vColor=vec4(0.9,0.9,0.9,1.0);
    }
}