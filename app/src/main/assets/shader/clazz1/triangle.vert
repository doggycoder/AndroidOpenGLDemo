//顶点坐标
attribute vec4 aVertexCo;
//顶点颜色
attribute vec4 aColor;

varying vec4 vColor;

void main() {
	gl_Position = aVertexCo;
	vColor = aColor;
}
