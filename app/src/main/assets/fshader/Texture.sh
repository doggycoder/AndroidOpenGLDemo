precision lowp float;

varying vec2 vCoord;
uniform sampler2D vTexture;
void main()
{
 gl_FragColor = texture2D(vTexture, vCoord);
}