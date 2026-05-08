package net.electrovoyage.customtrainspeeds;

import com.simibubi.create.foundation.gui.widget.ScrollInput;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;

public class ScrollInputWrapper {
	public ScrollInput input;
	public final Integer x;
	public final Integer y;
	public Font font;
	public Function<ScrollInputWrapper, String> formatter;
	public @Nullable String format;

	public int value() {return this.input.getState();}
	public boolean visible() {return this.input.visible;}

	public void visible(boolean state) {this.input.visible = this.input.active = state;}

	public ScrollInputWrapper(String format,
							  int x,
							  int y,
							  int width,
							  int height,
							  int min,
							  int max,
							  MutableComponent title) {
		this(x, y, width, height, min, max, title, null, format);
	}

	public ScrollInputWrapper(int x,
							  int y,
							  int width,
							  int height,
							  int min,
							  int max,
							  MutableComponent title,
							  @Nullable Function<ScrollInputWrapper, String> formatter,
							  @Nullable String format
	) {
		input = new ScrollInput(x, y, width, height)
			.titled(title)
			.withRange(min, max)
			.withStepFunction(ctx -> input.standardStep()
				.apply(ctx));

		visible(true);
		this.x = x;
		this.y = y;
		this.formatter = Objects.requireNonNullElse(formatter, defaultFormatter);
		this.format = format;
	}

	public void AddRenderable(Screen self) {
		try {
			Method method = Screen.class.getDeclaredMethod(
				"addRenderableWidget",
				GuiEventListener.class
			);
			method.setAccessible(true);
			method.invoke(self, input);

			// might as well also get the font
			font = self.font;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Function<ScrollInputWrapper, String> defaultFormatter = (self) -> String.format(format, self.value());

	public void Render(GuiGraphics graphics) {
		if (input.visible)
			graphics.drawString(font, formatter.apply(this), x, y, 0xFFFFFFFF, true);
	}
}
