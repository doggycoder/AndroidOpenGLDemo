attribute vec4 aPosition;
attribute vec2 aCoord;
attribute vec3 aNormal;
uniform mat4 uMatrix;
uniform vec4 uBaseColor;
uniform vec3 uLightColor;
uniform float uAmbientStrength;
uniform float uDiffuseStrength;
uniform float uSpecularStrength;
uniform vec3 uLightPosition;
varying vec4 vColor;

//在片元着色器中计算光照会获得更好更真实的光照效果，但是会比较耗性能

//环境光的计算
vec4 ambientColor(){
    vec3 ambient = uAmbientStrength * uLightColor;
    return vec4(ambient,1.0);
}

//漫反射的计算
vec4 diffuseColor(){
    //模型变换后的位置
    vec3 fragPos=(uMatrix*aPosition).xyz;
    //光照方向
    vec3 direction=normalize(uLightPosition-fragPos);
    //模型变换后的法线向量
    vec3 normal=normalize(mat3(uMatrix)*aNormal);
    //max(cos(入射角)，0)
    float diff = max(dot(normal,direction), 0.0);
    //材质的漫反射系数*max(cos(入射角)，0)*光照颜色
    vec3 diffuse=uDiffuseStrength * diff * uLightColor;
    return vec4(diffuse,1.0);
}

//镜面光计算，镜面光计算有两种方式，一种是冯氏模型，一种是Blinn改进的冯氏模型
//这里使用的是改进的冯氏模型，基于Half-Vector的计算方式
vec4 specularColor(){
    //模型变换后的位置
    vec3 fragPos=(uMatrix*aPosition).xyz;
    //光照方向
    vec3 lightDirection=normalize(uLightPosition-fragPos);
    //模型变换后的法线向量
    vec3 normal=normalize(mat3(uMatrix)*aNormal);
    //观察方向，这里将观察点固定在（0，0，uLightPosition.z）处
    vec3 viewDirection=normalize(vec3(0,0,uLightPosition.z)-fragPos);
    //观察向量与光照向量的半向量
    vec3 hafVector=normalize(lightDirection+viewDirection);
    //max(0,cos(半向量与法向量的夹角)^粗糙度
    float diff=pow(max(dot(normal,hafVector),0.0),4.0);
    //材质的镜面反射系数*max(0,cos(反射向量与观察向量夹角)^粗糙度*光照颜色
    //材质的镜面反射系数*max(0,cos(半向量与法向量的夹角)^粗糙度*光照颜色
    vec3 specular=uSpecularStrength*diff*uLightColor;
    return vec4(specular,1.0);
}

void main(){
    gl_Position=uMatrix*aPosition;
    vColor=(ambientColor() + diffuseColor() + specularColor())* uBaseColor;
}