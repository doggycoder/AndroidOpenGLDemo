attribute vec4 aPosition;
attribute vec2 aCoord;
varying vec2 vCoord;
varying vec2 vBlurCoord1s[14];
const highp float mWidth=720.0;
const highp float mHeight=1280.0;
uniform mat4 uMatrix;
void main( )
{
    gl_Position = uMatrix*aPosition;
    vCoord = aCoord;

    highp float mul_x = 2.0 / mWidth;
    highp float mul_y = 2.0 / mHeight;

    vBlurCoord1s[0] = vCoord + vec2( 0.0 * mul_x, -10.0 * mul_y );
    vBlurCoord1s[1] = vCoord + vec2( 8.0 * mul_x, -5.0 * mul_y );
    vBlurCoord1s[2] = vCoord + vec2( 8.0 * mul_x, 5.0 * mul_y );
    vBlurCoord1s[3] = aCoord + vec2( 0.0 * mul_x, 10.0 * mul_y );
    vBlurCoord1s[4] = aCoord + vec2( -8.0 * mul_x, 5.0 * mul_y );
    vBlurCoord1s[5] = aCoord + vec2( -8.0 * mul_x, -5.0 * mul_y );

    mul_x = 1.2 / mWidth;
    mul_y = 1.2 / mHeight;

    vBlurCoord1s[6] = aCoord + vec2( 0.0 * mul_x, -6.0 * mul_y );
    vBlurCoord1s[7] = aCoord + vec2( -4.0 * mul_x, -4.0 * mul_y );
    vBlurCoord1s[8] = aCoord + vec2( -6.0 * mul_x, 0.0 * mul_y );
    vBlurCoord1s[9] = aCoord + vec2( -4.0 * mul_x, 4.0 * mul_y );
    vBlurCoord1s[10] = aCoord + vec2( 0.0 * mul_x, 6.0 * mul_y );
    vBlurCoord1s[11] = aCoord + vec2( 4.0 * mul_x, 4.0 * mul_y );
    vBlurCoord1s[12] = aCoord + vec2( 6.0 * mul_x, 0.0 * mul_y );
    vBlurCoord1s[13] = aCoord + vec2( 4.0 * mul_x, -4.0 * mul_y );
}