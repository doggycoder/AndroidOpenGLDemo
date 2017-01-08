precision mediump float;
varying vec2 textureCoordinate;
uniform sampler2D vTexture;
varying vec4 vDiffuse;//接收从顶点着色器过来的散射光分量
void main() {
    vec4 finalColor=vec4(1.0);
       //给此片元颜色值
    gl_FragColor=finalColor*vDiffuse+finalColor*vec4(0.15,0.15,0.15,1.0);
}