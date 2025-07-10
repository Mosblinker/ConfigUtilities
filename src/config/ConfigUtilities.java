/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.nio.*;
import java.util.prefs.Preferences;

/**
 * A utility library used with configuration stuff.
 * @author Mosblinker
 */
public final class ConfigUtilities {
    /**
     * This is the header short for dimensions stored in byte arrays. This 
     * should result in a Base64 encoded String that starts with "DM".
     */
    public static final short DIMENSION_BYTE_ARRAY_HEADER = (short) 0x0CC3;
    /**
     * This is the header short for points stored in byte arrays. This should 
     * result in a Base64 encoded String that starts with "PT".
     */
    public static final short POINT_BYTE_ARRAY_HEADER = (short) 0x3D30;
    /**
     * This is the header short for rectangles stored in byte arrays. This 
     * should result in a Base64 encoded String that starts with "RT".
     */
    public static final short RECTANGLE_BYTE_ARRAY_HEADER = (short) 0x4530;
    /**
     * This is the header short for colors stored in byte arrays. This should 
     * result in a Base64 encoded String that starts with "CL".
     */
    public static final short COLOR_BYTE_ARRAY_HEADER = (short) 0x08B0;
    /**
     * This class cannot be constructed.
     */
    private ConfigUtilities() {}
    /**
     * 
     * @param arr
     * @param length
     * @return 
     */
    public static byte[] expandByteArray(byte[] arr, int length){
            // If the given byte array is null
        if (arr == null)
                // Create a new array with the correct length
            arr = new byte[length];
            // If the given byte array is too short
        else if (arr.length < length){
                // Store the array in a temporary variable
            byte[] temp = arr;
                // Create a new array with the correct length
            arr = new byte[length];
                // Copy the contents of the old array into the new array
            System.arraycopy(temp, 0, arr, 0, temp.length);
        }
        return arr;
    }
    /**
     * 
     * @param header
     * @param values
     * @return 
     */
    protected static byte[] intArraytoByteArray(short header, int... values){
            // This will get a byte array representation of the given integers
        byte[] arr = new byte[Short.BYTES+(Integer.BYTES*values.length)];
            // Wrap the byte array with a byte buffer to write to it
        ByteBuffer buffer = ByteBuffer.wrap(arr);
            // Add the given header to the buffer. This will signify what type 
            // of data this byte array represents.
        buffer.putShort(header);
            // Go through the integers in the given array
        for (int i : values){
                // Add the current integer to the buffer
            buffer.putInt(i);
        }
        return arr;
    }
    /**
     * 
     * @param header
     * @param value
     * @return 
     */
    protected static IntBuffer toIntBuffer(short header, byte[] value){
            // Wrap the byte array with a read only byte buffer to read from it
        ByteBuffer buffer = ByteBuffer.wrap(value).asReadOnlyBuffer();
        try{    // If the byte array's header matches the given header
            if (buffer.getShort() == header){
                    // Return the byte buffer as an IntBuffer
                return buffer.asIntBuffer();
            }
        } catch (BufferUnderflowException ex){ }
        return null;
    }
    /**
     * 
     * @param width
     * @param height
     * @return 
     */
    public static byte[] dimensionToByteArray(int width, int height){
        return intArraytoByteArray(DIMENSION_BYTE_ARRAY_HEADER,width,height);
    }
    /**
     * 
     * @param value
     * @return 
     */
    public static byte[] dimensionToByteArray(Dimension value){
            // If the given dimension object is null
        if (value == null)
            return null;
        return dimensionToByteArray(value.width,value.height);
    }
    /**
     * 
     * @param value
     * @param defaultValue
     * @return 
     */
    public static Dimension dimensionFromByteArray(byte[] value, 
            Dimension defaultValue){
            // If the given array is null
        if (value == null)
            return defaultValue;
            // Get an integer buffer to get the values for the dimension
        IntBuffer buffer = toIntBuffer(DIMENSION_BYTE_ARRAY_HEADER,value);
            // If the buffer is null (did not match the header) or there aren't 
            // 2 integers in the buffer
        if (buffer == null || buffer.remaining() != 2)
            return defaultValue;
        return new Dimension(buffer.get(),buffer.get());
    }
    /**
     * 
     * @param value
     * @return 
     */
    public static Dimension dimensionFromByteArray(byte[] value){
        return dimensionFromByteArray(value,null);
    }
    /**
     * 
     * @param x
     * @param y
     * @return 
     */
    public static byte[] pointToByteArray(int x, int y){
        return intArraytoByteArray(POINT_BYTE_ARRAY_HEADER,x,y);
    }
    /**
     * 
     * @param value
     * @return 
     */
    public static byte[] pointToByteArray(Point value){
            // If the given point object is null
        if (value == null)
            return null;
        return pointToByteArray(value.x,value.y);
    }
    /**
     * 
     * @param value
     * @param defaultValue
     * @return 
     */
    public static Point pointFromByteArray(byte[] value, Point defaultValue){
            // If the given array is null
        if (value == null)
            return defaultValue;
            // Get an integer buffer to get the values for the point
        IntBuffer buffer = toIntBuffer(POINT_BYTE_ARRAY_HEADER,value);
            // If the buffer is null (did not match the header) or there aren't 
            // 2 integers in the buffer
        if (buffer == null || buffer.remaining() != 2)
            return defaultValue;
        return new Point(buffer.get(),buffer.get());
    }
    /**
     * 
     * @param value
     * @return 
     */
    public static Point pointFromByteArray(byte[] value){
        return pointFromByteArray(value,null);
    }
    /**
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     * @return 
     */
    public static byte[] rectangleToByteArray(int x,int y,int width,int height){
        return intArraytoByteArray(RECTANGLE_BYTE_ARRAY_HEADER,x,y,width,height);
    }
    /**
     * 
     * @param width
     * @param height
     * @return 
     */
    public static byte[] rectangleToByteArray(int width,int height){
        return rectangleToByteArray(0,0,width,height);
    }
    /**
     * 
     * @param value
     * @return 
     */
    public static byte[] rectangleToByteArray(Rectangle value){
            // If the given rectangle object is null
        if (value == null)
            return null;
            // Convert the rectangle object into an array of integers, and 
            // convert that into an array of bytes
        return rectangleToByteArray(value.x,value.y,value.width,value.height);
    }
    /**
     * 
     * @param value
     * @param defaultValue
     * @return 
     */
    public static Rectangle rectangleFromByteArray(byte[] value, 
            Rectangle defaultValue){
            // If the given array is null
        if (value == null)
            return defaultValue;
            // Get an integer buffer to get the values for the rectangle
        IntBuffer buffer = toIntBuffer(RECTANGLE_BYTE_ARRAY_HEADER,value);
            // If the buffer is not null (the byte array matched the header)
        if (buffer != null){
                // If there are two integers in the buffer
            if (buffer.remaining() == 2)
                return new Rectangle(buffer.get(),buffer.get());
                // If there are four integers in the buffer
            else if (buffer.remaining() == 4)
                return new Rectangle(buffer.get(),buffer.get(),
                        buffer.get(),buffer.get());
        }
        return defaultValue;
    }
    /**
     * 
     * @param value
     * @return 
     */
    public static Rectangle rectangleFromByteArray(byte[] value){
        return rectangleFromByteArray(value,null);
    }
    /**
     * 
     * @param color
     * @return 
     */
    public static String colorToString(Color color){
            // If the color is null
        if (color == null)
            return null;
        return String.format("%08X", color.getRGB());
    }
    /**
     * 
     * @param value
     * @param defaultValue
     * @return 
     */
    public static Color colorFromString(String value, Color defaultValue){
            // If the given value is not null
        if (value != null){
                // Try to parse the hexadecimal string and get the color from 
                // the given string. If there are more than 6 characters in the 
            try{// string, then the alpha component was specified 
                return new Color(Integer.parseUnsignedInt(value,16),
                        value.length()>6);
            } catch(NumberFormatException ex){ }
        }
        return defaultValue;
    }
    /**
     * 
     * @param color
     * @return 
     */
    public static byte[] colorToByteArray(Color color){
            // If the color is null
        if (color == null)
            return null;
        return intArraytoByteArray(COLOR_BYTE_ARRAY_HEADER,color.getRGB());
    }
    /**
     * 
     * @param value
     * @param defaultValue
     * @return 
     */
    public static Color colorFromByteArray(byte[] value, Color defaultValue){
            // If the given value is not null and its length is greater than 2 
            // and its length is less than or equal to 6
        if (value != null && value.length > 2 && value.length <= Short.BYTES+Integer.BYTES){
                // Get the header as a short
            short header = (short)((Byte.toUnsignedInt(value[0]) << Byte.SIZE) | 
                    Byte.toUnsignedInt(value[1]));
               // If the byte array's header matches the color header
            if (header == COLOR_BYTE_ARRAY_HEADER){
                    // This gets if the byte array encodes an alpha component
                boolean hasAlpha = value.length == Short.BYTES+Integer.BYTES;
                    // This will get the RGB value from the byte array
                int rgb = 0;
                    // Go through the remaining bytes in the array
                for (int i = 2; i < value.length; i++){
                        // Shift over the current value by one byte
                    rgb <<= Byte.SIZE;
                        // Append the next byte in the array
                    rgb |= Byte.toUnsignedInt(value[i]);
                }
                return new Color(rgb,hasAlpha);
            }
        }
        return defaultValue;
    }
    /**
     * 
     * @param value
     * @return 
     */
    public static Boolean booleanValueOf(String value){
            // If the value is equal to the word "true", ignoring case
        if ("true".equalsIgnoreCase(value))
            return true;
            // If the value is equal to the word "false", ignoring case
        else if ("false".equalsIgnoreCase(value))
            return false;
        return null;
    }
    /**
     * 
     * @param node
     * @param key
     * @param defaultValue
     * @return 
     */
    public static Dimension getDimension(Preferences node, String key, 
            Dimension defaultValue){
        return dimensionFromByteArray(node.getByteArray(key, null), 
                defaultValue);
    }
    /**
     * 
     * @param node
     * @param key
     * @param width
     * @param height 
     */
    public static void putDimension(Preferences node, String key, 
            int width, int height){
        node.putByteArray(key, dimensionToByteArray(width, height));
    }
    /**
     * 
     * @param node
     * @param key
     * @param value 
     */
    public static void putDimension(Preferences node, String key, 
            Dimension value){
            // If the dimension object is null
        if (value == null)
                // Remove the key
            node.remove(key);
        else 
            putDimension(node,key,value.width,value.height);
    }
    /**
     * 
     * @param node
     * @param key
     * @param comp 
     */
    public static void putDimension(Preferences node, String key, Component comp){
        putDimension(node,key,comp.getWidth(),comp.getHeight());
    }
    /**
     * 
     * @param node
     * @param key
     * @param defaultValue
     * @return 
     */
    public static Point getPoint(Preferences node, String key, Point defaultValue){
        return pointFromByteArray(node.getByteArray(key, null), 
                defaultValue);
    }
    /**
     * 
     * @param node
     * @param key
     * @param x
     * @param y 
     */
    public static void putPoint(Preferences node, String key, int x, int y){
        node.putByteArray(key, pointToByteArray(x, y));
    }
    /**
     * 
     * @param node
     * @param key
     * @param value 
     */
    public static void putPoint(Preferences node, String key, Point value){
            // If the point object is null
        if (value == null)
                // Remove the key
            node.remove(key);
        else 
            putPoint(node,key,value.x,value.y);
    }
    /**
     * 
     * @param node
     * @param key
     * @param comp 
     */
    public static void putPoint(Preferences node, String key, Component comp){
        putPoint(node,key,comp.getX(),comp.getY());
    }
    /**
     * 
     * @param node
     * @param key
     * @param defaultValue
     * @return 
     */
    public static Rectangle getRectangle(Preferences node, String key, 
            Rectangle defaultValue){
        return rectangleFromByteArray(node.getByteArray(key, null), defaultValue);
    }
    /**
     * 
     * @param node
     * @param key
     * @param x
     * @param y
     * @param width
     * @param height 
     */
    public static void putRectangle(Preferences node, String key, int x, int y, 
            int width, int height){
        node.putByteArray(key, rectangleToByteArray(x, y, width, height));
    }
    /**
     * 
     * @param node
     * @param key
     * @param width
     * @param height 
     */
    public static void putRectangle(Preferences node, String key, int width, int height){
        putRectangle(node,key,0,0,width,height);
    }
    /**
     * 
     * @param node
     * @param key
     * @param value 
     */
    public static void putRectangle(Preferences node, String key, Rectangle value){
            // If the rectangle object is null
        if (value == null)
                // Remove the key
            node.remove(key);
        else 
            putRectangle(node,key,value.x,value.y,value.width,value.height);
    }
    /**
     * 
     * @param node
     * @param key
     * @param comp 
     */
    public void putRectangle(Preferences node, String key, Component comp){
        putRectangle(node,key,comp.getX(),comp.getY(),comp.getWidth(),comp.getHeight());
    }
}
