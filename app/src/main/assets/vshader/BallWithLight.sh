uniform mat4 vMatrix;        //总变换矩阵
uniform mat4 uMMatrix;          //变换矩阵
uniform vec3 uLightLocation;        //光源位置
uniform vec3 uCamera;           //摄像机位置
attribute vec3 vPosition;       //顶点位置
attribute vec3 vNormal;         //法向量
varying vec3 fPosition;         //用于传递给片元着色器的顶点位置
varying vec4 vAmbient;          //用于传递给片元着色器的环境光最终强度
varying vec4 vDiffuse;          //用于传递给片元着色器的散射光最终强度
varying vec4 vSpecular;         //用于传递给片元着色器的镜面光最终强度

void pointLight(                    //定位光光照计算的方法
  vec3 normal,               //法向量
  vec4 ambient,           //环境光最终强度
  vec4 diffuse,               //散射光最终强度
  vec4 specular,          //镜面光最终强度
  vec3 lightLocation,            //光源位置
  vec4 lightAmbient,         //环境光强度
  vec4 lightDiffuse,         //散射光强度
  vec4 lightSpecular         //镜面光强度
){
  ambient=lightAmbient;         //直接得出环境光的最终强度
  vec3 normalTarget=vPosition+normal;   //计算变换后的法向量
  vec3 newNormal=(vMatrix*vec4(normalTarget,1)).xyz-(vMatrix*vec4(vPosition,1)).xyz;
  newNormal=normalize(newNormal);   //对法向量规格化
  //计算从表面点到摄像机的向量
  vec3 eye= normalize(/*uCamera*/vec3(3.0,-8.0,-2.0)-(vMatrix*vec4(vPosition,1)).xyz);
  //计算从表面点到光源位置的向量vp
  vec3 vp= normalize(lightLocation-(vMatrix*vec4(vPosition,1)).xyz);
  vp=normalize(vp);//格式化vp
  vec3 halfVector=normalize(vp+eye);    //求视线与光线的半向量
  float shininess=50.0;             //粗糙度，越小越光滑
  float nDotViewPosition=max(0.0,dot(newNormal,vp));    //求法向量与vp的点积与0的最大值
  diffuse=lightDiffuse*nDotViewPosition;                //计算散射光的最终强度
  float nDotViewHalfVector=dot(newNormal,halfVector);   //法线与半向量的点积
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess));     //镜面反射光强度因子
  specular=lightSpecular*powerFactor;               //计算镜面光的最终强度
}
void main(){
   gl_Position = vMatrix * vec4(vPosition,1); //根据总变换矩阵计算此次绘制此顶点位置
   vec4 ambientTemp,diffuseTemp,specularTemp;     //用来接收三个通道最终强度的变量
   pointLight(normalize(/*vNormal*/vec3(0.0,1.0,0.0)),ambientTemp,diffuseTemp,specularTemp,vec3(3.0,3.0,3.0)/*uLightLocation*/,
   vec4(0.15,0.15,0.15,1.0),vec4(0.8,0.8,0.8,1.0),vec4(0.7,0.7,0.7,1.0));
   vAmbient=ambientTemp;        //将环境光最终强度传给片元着色器
   vDiffuse=diffuseTemp;        //将散射光最终强度传给片元着色器
   vSpecular=specularTemp;      //将镜面光最终强度传给片元着色器
   fPosition = vPosition;  //将顶点的位置传给片元着色器
}