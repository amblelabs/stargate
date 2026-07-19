package dev.amblelabs.stargate.api.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteOpenHashSet;

import java.util.*;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public abstract class CustomComponentTagVisitor implements TagVisitor {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int INLINE_LIST_THRESHOLD = 8;
    private static final int MAX_DEPTH = 64;
    private static final int MAX_LENGTH = 128;

    private static final ByteCollection INLINE_ELEMENT_TYPES = new ByteOpenHashSet(Arrays.asList((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6));
    private static final ChatFormatting SYNTAX_HIGHLIGHTING_KEY = ChatFormatting.AQUA;
    private static final ChatFormatting SYNTAX_HIGHLIGHTING_STRING = ChatFormatting.GREEN;
    private static final ChatFormatting SYNTAX_HIGHLIGHTING_NUMBER = ChatFormatting.GOLD;
    private static final ChatFormatting SYNTAX_HIGHLIGHTING_NUMBER_TYPE = ChatFormatting.RED;

    private static final String LIST_OPEN = "[";
    private static final String LIST_CLOSE = "]";
    private static final String LIST_TYPE_SEPARATOR = ";";
    private static final String ELEMENT_SPACING = " ";
    private static final String STRUCT_OPEN = "{";
    private static final String STRUCT_CLOSE = "}";
    private static final String NEWLINE = "\n";

    private static final String NAME_VALUE_SEPARATOR = ": ";
    private static final String ELEMENT_SEPARATOR = ",";
    private static final String WRAPPED_ELEMENT_SEPARATOR = ELEMENT_SEPARATOR + NEWLINE;
    private static final String SPACED_ELEMENT_SEPARATOR = ELEMENT_SEPARATOR + ELEMENT_SPACING;

    private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");

    private static final Component FOLDED = Component.literal("<...>").withStyle(ChatFormatting.GRAY);
    private static final Component BYTE_TYPE = Component.literal("b").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
    private static final Component SHORT_TYPE = Component.literal("s").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
    private static final Component INT_TYPE = Component.literal("I").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
    private static final Component LONG_TYPE = Component.literal("L").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
    private static final Component FLOAT_TYPE = Component.literal("f").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
    private static final Component DOUBLE_TYPE = Component.literal("d").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
    private static final Component BYTE_ARRAY_TYPE = Component.literal("B").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);

    private final String indentation;
    private final MutableComponent result = Component.empty();

    private int indentDepth;
    private int depth;

    public CustomComponentTagVisitor(String indentation) {
        this.indentation = indentation;
    }

    public abstract @Nullable Tag onTag(String key, Tag tag);

    public Component visit(Tag tag) {
        tag.accept(this);
        return this.result;
    }

    public void visitString(StringTag tag) {
        String string = StringTag.quoteAndEscape(tag.getAsString());
        String string2 = string.substring(0, 1);
        Component component = Component.literal(string.substring(1, string.length() - 1)).withStyle(SYNTAX_HIGHLIGHTING_STRING);
        this.result.append(string2).append(component).append(string2);
    }

    public void visitByte(ByteTag tag) {
        this.result.append(Component.literal(String.valueOf(tag.getAsNumber())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)).append(BYTE_TYPE);
    }

    public void visitShort(ShortTag tag) {
        this.result.append(Component.literal(String.valueOf(tag.getAsNumber())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)).append(SHORT_TYPE);
    }

    public void visitInt(IntTag tag) {
        this.result.append(Component.literal(String.valueOf(tag.getAsNumber())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER));
    }

    public void visitLong(LongTag tag) {
        this.result.append(Component.literal(String.valueOf(tag.getAsNumber())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)).append(LONG_TYPE);
    }

    public void visitFloat(FloatTag tag) {
        this.result.append(Component.literal(String.valueOf(tag.getAsFloat())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)).append(FLOAT_TYPE);
    }

    public void visitDouble(DoubleTag tag) {
        this.result.append(Component.literal(String.valueOf(tag.getAsDouble())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)).append(DOUBLE_TYPE);
    }

    public void visitByteArray(ByteArrayTag tag) {
        this.result.append(LIST_OPEN).append(BYTE_ARRAY_TYPE).append(LIST_TYPE_SEPARATOR);
        byte[] bs = tag.getAsByteArray();

        for(int i = 0; i < bs.length && i < MAX_LENGTH; ++i) {
            MutableComponent mutableComponent = Component.literal(String.valueOf(bs[i])).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
            this.result.append(ELEMENT_SPACING).append(mutableComponent).append(BYTE_ARRAY_TYPE);
            if (i != bs.length - 1) {
                this.result.append(ELEMENT_SEPARATOR);
            }
        }

        if (bs.length > MAX_LENGTH) {
            this.result.append(FOLDED);
        }

        this.result.append(LIST_CLOSE);
    }

    public void visitIntArray(IntArrayTag tag) {
        this.result.append(LIST_OPEN).append(INT_TYPE).append(LIST_TYPE_SEPARATOR);
        int[] is = tag.getAsIntArray();

        for(int i = 0; i < is.length && i < MAX_LENGTH; ++i) {
            this.result.append(ELEMENT_SPACING).append(Component.literal(String.valueOf(is[i])).withStyle(SYNTAX_HIGHLIGHTING_NUMBER));
            if (i != is.length - 1) {
                this.result.append(ELEMENT_SEPARATOR);
            }
        }

        if (is.length > MAX_LENGTH) {
            this.result.append(FOLDED);
        }

        this.result.append(LIST_CLOSE);
    }

    public void visitLongArray(LongArrayTag tag) {
        this.result.append(LIST_OPEN).append(LONG_TYPE).append(LIST_TYPE_SEPARATOR);
        long[] ls = tag.getAsLongArray();

        for(int i = 0; i < ls.length && i < MAX_LENGTH; ++i) {
            Component component = Component.literal(String.valueOf(ls[i])).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
            this.result.append(ELEMENT_SPACING).append(component).append(LONG_TYPE);
            if (i != ls.length - 1) {
                this.result.append(ELEMENT_SEPARATOR);
            }
        }

        if (ls.length > MAX_LENGTH) {
            this.result.append(FOLDED);
        }

        this.result.append(LIST_CLOSE);
    }

    public void visitList(ListTag tag) {
        if (tag.isEmpty()) {
            this.result.append(LIST_OPEN + LIST_CLOSE);
        } else if (this.depth >= MAX_DEPTH) {
            this.result.append(LIST_OPEN).append(FOLDED).append(LIST_CLOSE);
        } else if (INLINE_ELEMENT_TYPES.contains(tag.getElementType()) && tag.size() <= INLINE_LIST_THRESHOLD) {
            this.result.append(LIST_OPEN);

            for(int i = 0; i < tag.size(); ++i) {
                if (i != 0) {
                    this.result.append(SPACED_ELEMENT_SEPARATOR);
                }

                this.appendSubTag(tag.get(i), false);
            }

            this.result.append(LIST_CLOSE);
        } else {
            this.result.append(LIST_OPEN);
            if (!this.indentation.isEmpty()) {
                this.result.append(NEWLINE);
            }

            String string = Strings.repeat(this.indentation, this.indentDepth + 1);

            for(int j = 0; j < tag.size() && j < MAX_LENGTH; ++j) {
                this.result.append(string);
                this.appendSubTag(tag.get(j), true);
                if (j != tag.size() - 1) {
                    this.result.append(this.indentation.isEmpty() ? SPACED_ELEMENT_SEPARATOR : WRAPPED_ELEMENT_SEPARATOR);
                }
            }

            if (tag.size() > MAX_LENGTH) {
                this.result.append(string).append(FOLDED);
            }

            if (!this.indentation.isEmpty()) {
                this.result.append(NEWLINE + Strings.repeat(this.indentation, this.indentDepth));
            }

            this.result.append(LIST_CLOSE);
        }
    }

    public void visitCompound(CompoundTag tag) {
        if (tag.isEmpty()) {
            this.result.append(STRUCT_OPEN + STRUCT_CLOSE);
        } else if (this.depth >= MAX_DEPTH) {
            this.result.append(STRUCT_OPEN).append(FOLDED).append(STRUCT_CLOSE);
        } else {
            this.result.append(STRUCT_OPEN);
            Collection<String> collection = tag.getAllKeys();
            if (LOGGER.isDebugEnabled()) {
                List<String> list = Lists.newArrayList(tag.getAllKeys());
                Collections.sort(list);
                collection = list;
            }

            if (!this.indentation.isEmpty()) {
                this.result.append(NEWLINE);
            }

            String string = Strings.repeat(this.indentation, this.indentDepth + 1);
            Iterator<String> iterator = collection.iterator();

            while (iterator.hasNext()) {
                String key = iterator.next();
                this.result.append(string).append(handleEscapePretty(key)).append(NAME_VALUE_SEPARATOR);

                Tag childTag = this.onTag(key, Objects.requireNonNull(tag.get(key)));
                if (childTag == null) continue;

                this.appendSubTag(childTag, true);
                if (iterator.hasNext()) {
                    this.result.append(this.indentation.isEmpty() ? SPACED_ELEMENT_SEPARATOR : WRAPPED_ELEMENT_SEPARATOR);
                }
            }

            if (!this.indentation.isEmpty()) {
                this.result.append(NEWLINE + Strings.repeat(this.indentation, this.indentDepth));
            }

            this.result.append(STRUCT_CLOSE);
        }
    }

    private void appendSubTag(Tag tag, boolean indent) {
        if (indent) {
            ++this.indentDepth;
        }

        ++this.depth;

        try {
            tag.accept(this);
        } finally {
            if (indent) {
                --this.indentDepth;
            }

            --this.depth;
        }

    }

    protected static Component handleEscapePretty(String text) {
        if (SIMPLE_VALUE.matcher(text).matches()) {
            return Component.literal(text).withStyle(SYNTAX_HIGHLIGHTING_KEY);
        } else {
            String string = StringTag.quoteAndEscape(text);
            String string2 = string.substring(0, 1);
            Component component = Component.literal(string.substring(1, string.length() - 1)).withStyle(SYNTAX_HIGHLIGHTING_KEY);
            return Component.literal(string2).append(component).append(string2);
        }
    }

    public void visitEnd(EndTag tag) {
    }
}
