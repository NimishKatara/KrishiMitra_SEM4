package com.example.krishimitra;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Paint;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class GradientTextView extends AppCompatTextView {

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = getPaint();
        float width = paint.measureText(getText().toString());

        Shader textShader = new LinearGradient(
                0, 0, width, 0,
                new int[]{0xFFDC7222,0xFFAAC4F1,0xFF33932A}, // Orange to Green (Adjust as needed)
                null,
                Shader.TileMode.CLAMP
        );

        paint.setShader(textShader);

        super.onDraw(canvas); // Draw the text with gradient
    }
}
