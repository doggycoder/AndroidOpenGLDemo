precision lowp float;
precision lowp int;
uniform sampler2D vTexture;
uniform int iternum;
uniform float aaCoef; //参数
uniform float mixCoef; //混合系数
varying highp vec2 textureCoordinate;
varying highp vec2 blurCoord1s[14];
const float distanceNormalizationFactor = 4.0;    //标准化距离因子常量
const mat3 saturateMatrix = mat3(1.1102,-0.0598,-0.061,-0.0774,1.0826,-0.1186,-0.0228,-0.0228,1.1772);

void main( ) {

    vec3 centralColor;
    float central;
    float gaussianWeightTotal;
    float sum;
    float sampleColor;
    float distanceFromCentralColor;
    float gaussianWeight;

    //通过绿色通道来磨皮
    //取得当前点颜色的绿色通道
    central = texture2D( vTexture, textureCoordinate ).g;
    //高斯权重
    gaussianWeightTotal = 0.2;
    //绿色通道色彩记数
    sum = central * 0.2;

    // 计算各个采样点处的高斯权重，包括密闭性和相似性
    for (int i = 0; i < 6; i++) {
        //采样点的绿色通道
        sampleColor = texture2D( vTexture, blurCoord1s[i] ).g;
        //采样点和计算点的颜色差
        distanceFromCentralColor = min( abs( central - sampleColor ) * distanceNormalizationFactor, 1.0 );
        //高斯权重
        gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);
        //高斯权重总和
        gaussianWeightTotal += gaussianWeight;
        //绿色通道色彩记数累计
        sum += sampleColor * gaussianWeight;
    }
    for (int i = 6; i < 14; i++) {
        //采样点的绿色通道
        sampleColor = texture2D( vTexture, blurCoord1s[i] ).g;
        //采样点和计算点的颜色差
        distanceFromCentralColor = min( abs( central - sampleColor ) * distanceNormalizationFactor, 1.0 );
        //高斯权重
        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);
        //高斯权重总和
        gaussianWeightTotal += gaussianWeight;
        //绿色通道色彩记数累计
        sum += sampleColor * gaussianWeight;
    }

    //采样后的绿色通道色彩均值
    sum = sum / gaussianWeightTotal;

    //取得当前点的颜色
    centralColor = texture2D( vTexture, textureCoordinate ).rgb;
    //采样值
    sampleColor = centralColor.g - sum + 0.5;
    //迭代计算
    for (int i = 0; i < iternum; ++i) {
        if (sampleColor <= 0.5) {
            sampleColor = sampleColor * sampleColor * 2.0;
        }else {
            sampleColor = 1.0 - ((1.0 - sampleColor)*(1.0 - sampleColor) * 2.0);
        }
    }

    float aa = 1.0 + pow( centralColor.g, 0.3 )*aaCoef;
    vec3 smoothColor = centralColor*aa - vec3( sampleColor )*(aa - 1.0);
    smoothColor = clamp( smoothColor, vec3( 0.0 ), vec3( 1.0 ) );
    smoothColor = mix( centralColor, smoothColor, pow( centralColor.g, 0.33 ) );
    smoothColor = mix( centralColor, smoothColor, pow( centralColor.g, mixCoef ) );
    gl_FragColor = vec4( pow( smoothColor, vec3( 0.96 ) ), 1.0 );
    vec3 satcolor = gl_FragColor.rgb * saturateMatrix;
    gl_FragColor.rgb = mix( gl_FragColor.rgb, satcolor, 0.23 );

}