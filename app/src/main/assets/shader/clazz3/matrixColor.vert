//顶点坐标
attribute vec4 aVertexCo;
//顶点变换矩阵
uniform mat4 uVertexMat;

attribute vec4 aColor;

varying vec4 vColor;

void main() {
	gl_Position = uVertexMat * aVertexCo;
	vColor = aColor;
}
