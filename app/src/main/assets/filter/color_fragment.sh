precision mediump float;

uniform sampler2D vTexture;
uniform int vChangeType;
uniform vec3 vChangeColor;

varying vec2 aCoordinate;

void modifyColor(vec4 color){
    color.r=max(min(color.r,1.0),0.0);
    color.g=max(min(color.g,1.0),0.0);
    color.b=max(min(color.b,1.0),0.0);
    color.a=max(min(color.a,1.0),0.0);
}

void main(){
    vec4 nColor=texture2D(vTexture,aCoordinate);
   if(vChangeType==1){
        float c=nColor.r*vChangeColor.r+nColor.g*vChangeColor.g+nColor.b*vChangeColor.b;
        gl_FragColor=vec4(c,c,c,nColor.a);
    }else if(vChangeType==2){
        vec4 deltaColor=nColor+vec4(vChangeColor,0.0);
        modifyColor(deltaColor);
        gl_FragColor=deltaColor;
    }else{
        gl_FragColor=nColor;
    }
}