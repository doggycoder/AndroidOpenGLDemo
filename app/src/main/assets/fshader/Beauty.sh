precision mediump float;

varying mediump vec2 vCoord;

uniform sampler2D vTexture;
uniform vec2 pStepOffset;
uniform mediump float pParams;

const highp vec3 W = vec3(0.299,0.587,0.114);
vec2 blurCoordinates[24];

float hardLight(float color)
{
    if(color <= 0.5)
    color = color * color * 2.0;
    else
    color = 1.0 - ((1.0 - color)*(1.0 - color) * 2.0);
    return color;
}

void main(){

    vec3 centralColor = texture2D(vTexture, vCoord).rgb;
    blurCoordinates[0] = vCoord.xy + pStepOffset * vec2(0.0, -10.0);
    blurCoordinates[1] = vCoord.xy + pStepOffset * vec2(0.0, 10.0);
    blurCoordinates[2] = vCoord.xy + pStepOffset * vec2(-10.0, 0.0);
    blurCoordinates[3] = vCoord.xy + pStepOffset * vec2(10.0, 0.0);
    blurCoordinates[4] = vCoord.xy + pStepOffset * vec2(5.0, -8.0);
    blurCoordinates[5] = vCoord.xy + pStepOffset * vec2(5.0, 8.0);
    blurCoordinates[6] = vCoord.xy + pStepOffset * vec2(-5.0, 8.0);
    blurCoordinates[7] = vCoord.xy + pStepOffset * vec2(-5.0, -8.0);
    blurCoordinates[8] = vCoord.xy + pStepOffset * vec2(8.0, -5.0);
    blurCoordinates[9] = vCoord.xy + pStepOffset * vec2(8.0, 5.0);
    blurCoordinates[10] = vCoord.xy + pStepOffset * vec2(-8.0, 5.0);
    blurCoordinates[11] = vCoord.xy + pStepOffset * vec2(-8.0, -5.0);
    blurCoordinates[12] = vCoord.xy + pStepOffset * vec2(0.0, -6.0);
    blurCoordinates[13] = vCoord.xy + pStepOffset * vec2(0.0, 6.0);
    blurCoordinates[14] = vCoord.xy + pStepOffset * vec2(6.0, 0.0);
    blurCoordinates[15] = vCoord.xy + pStepOffset * vec2(-6.0, 0.0);
    blurCoordinates[16] = vCoord.xy + pStepOffset * vec2(-4.0, -4.0);
    blurCoordinates[17] = vCoord.xy + pStepOffset * vec2(-4.0, 4.0);
    blurCoordinates[18] = vCoord.xy + pStepOffset * vec2(4.0, -4.0);
    blurCoordinates[19] = vCoord.xy + pStepOffset * vec2(4.0, 4.0);
    blurCoordinates[20] = vCoord.xy + pStepOffset * vec2(-2.0, -2.0);
    blurCoordinates[21] = vCoord.xy + pStepOffset * vec2(-2.0, 2.0);
    blurCoordinates[22] = vCoord.xy + pStepOffset * vec2(2.0, -2.0);
    blurCoordinates[23] = vCoord.xy + pStepOffset * vec2(2.0, 2.0);

    float sampleColor = centralColor.g * 22.0;
    sampleColor += texture2D(vTexture, blurCoordinates[0]).g;    
    sampleColor += texture2D(vTexture, blurCoordinates[1]).g;    
    sampleColor += texture2D(vTexture, blurCoordinates[2]).g;    
    sampleColor += texture2D(vTexture, blurCoordinates[3]).g;    
    sampleColor += texture2D(vTexture, blurCoordinates[4]).g;    
    sampleColor += texture2D(vTexture, blurCoordinates[5]).g;    
    sampleColor += texture2D(vTexture, blurCoordinates[6]).g;    
    sampleColor += texture2D(vTexture, blurCoordinates[7]).g;    
    sampleColor += texture2D(vTexture, blurCoordinates[8]).g;    
    sampleColor += texture2D(vTexture, blurCoordinates[9]).g;    
    sampleColor += texture2D(vTexture, blurCoordinates[10]).g;    
    sampleColor += texture2D(vTexture, blurCoordinates[11]).g;    
    sampleColor += texture2D(vTexture, blurCoordinates[12]).g * 2.0;    
    sampleColor += texture2D(vTexture, blurCoordinates[13]).g * 2.0;    
    sampleColor += texture2D(vTexture, blurCoordinates[14]).g * 2.0;    
    sampleColor += texture2D(vTexture, blurCoordinates[15]).g * 2.0;    
    sampleColor += texture2D(vTexture, blurCoordinates[16]).g * 2.0;    
    sampleColor += texture2D(vTexture, blurCoordinates[17]).g * 2.0;    
    sampleColor += texture2D(vTexture, blurCoordinates[18]).g * 2.0;    
    sampleColor += texture2D(vTexture, blurCoordinates[19]).g * 2.0;    
    sampleColor += texture2D(vTexture, blurCoordinates[20]).g * 3.0;    
    sampleColor += texture2D(vTexture, blurCoordinates[21]).g * 3.0;    
    sampleColor += texture2D(vTexture, blurCoordinates[22]).g * 3.0;    
    sampleColor += texture2D(vTexture, blurCoordinates[23]).g * 3.0;    

    sampleColor = sampleColor / 62.0;

    float highPass = centralColor.g - sampleColor + 0.5;

    for(int i = 0; i < 5;i++)
    {
        highPass = hardLight(highPass);
    }
    float luminance = dot(centralColor, W);

    float alpha = pow(luminance, pParams);

    vec3 smoothColor = centralColor + (centralColor-vec3(highPass))*alpha*0.1;

    gl_FragColor = vec4(mix(smoothColor.rgb, max(smoothColor, centralColor), alpha), 1.0);
}