package edu.wuwang.opengl.tool;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.IntBuffer;

public class GlTool {

    public static String readShaderFromAssets(Context context, String path){
        try {
            InputStream stream = context.getAssets().open(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"UTF-8"));
            StringBuilder sb = new StringBuilder();
            do {
                String line = reader.readLine();
                if(line == null){
                    break;
                }
                sb.append(line);
                sb.append("\n");
            }while (true);
            stream.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int createShader(String shader,int type){
        int shaderId = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shaderId,shader);
        GLES20.glCompileShader(shaderId);
        int[] status = new int[1];
        GLES20.glGetShaderiv(shaderId,GLES20.GL_COMPILE_STATUS,IntBuffer.wrap(status));
        if(status[0] == 0){
            String info = GLES20.glGetShaderInfoLog(shaderId);
            Log.e("wuwang","shaderCompileError:"+type+"/"+shader+"/"+info);
            GLES20.glDeleteShader(shaderId);
            return -1;
        }
        return shaderId;
    }

    public static int compileProgramFromAssets(Context context,String vert,String frag){
        String vertexStr = readShaderFromAssets(context,vert);
        String fragmentStr = readShaderFromAssets(context,frag);
        int vertexShaderId = createShader(vertexStr,GLES20.GL_VERTEX_SHADER);
        if(vertexShaderId == -1){
            return -1;
        }
        int fragmentShaderId = createShader(fragmentStr,GLES20.GL_FRAGMENT_SHADER);
        if(fragmentShaderId == -1){
            return -1;
        }

        int programId = GLES20.glCreateProgram();
        GLES20.glAttachShader(programId,vertexShaderId);
        GLES20.glAttachShader(programId,fragmentShaderId);
        GLES20.glLinkProgram(programId);
        int[] status = new int[1];
        GLES20.glGetProgramiv(programId,GLES20.GL_LINK_STATUS,IntBuffer.wrap(status));

        if(status[0] == 0){
            String info = GLES20.glGetProgramInfoLog(programId);
            Log.e("wuwang","programLinkError:"+programId+"/"+"/"+info);
            GLES20.glDeleteShader(programId);
            return -1;
        }
        return programId;
    }

}
