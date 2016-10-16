precision mediump float;
//uniform float uR;
varying vec3 fPosition;//接收从顶点着色器过来的顶点位置
varying vec4 vAmbient;//接收从顶点着色器过来的环境光分量
varying vec4 vDiffuse;//接收从顶点着色器过来的散射光分量
varying vec4 vSpecular;//接收从顶点着色器过来的镜面反射光分量
void main()
{
   float uR=0.5;
   vec3 color;
   float n = 10.0;//一个坐标分量分的总份数
   float span = 2.0*uR/n;//每一份的长度
   //每一维在立方体内的行列数
   int i = int((fPosition.x + uR)/span);
   int j = int((fPosition.y + uR)/span);
   int k = int((fPosition.z + uR)/span);
   //计算当点应位于白色块还是黑色块中
   int whichColor = int(mod(float(i+j+k),2.0));
   if(whichColor == 1) {//奇数时为红色
        color = vec3(0.678,0.231,0.129);//红色
   }
   else {//偶数时为白色
        color = vec3(1.0,1.0,1.0);//白色
   }
   //最终颜色
   vec4 finalColor=vec4(color,1.0);
   //给此片元颜色值
   gl_FragColor=finalColor/*finalColor*vAmbient + finalColor*vDiffuse + finalColor*vSpecular*/;
}