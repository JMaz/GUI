package com.jamasztal;

public class Display
{
    private String contents;
    private int cursorPosition;
    
    public Display()
    {
       this("",0);
    }
    
    public Display(String s, int i)
    {
        contents = s;
        cursorPosition = i;
    }
    
    public String getContents()
    {
        return contents;
    }
    
    public void move(int n)
    {
        cursorPosition += n;
        if (cursorPosition < 0)
            cursorPosition = 0;
        else if (cursorPosition > contents.length() )
            cursorPosition = contents.length();
    }
        
    public void moveLeft()
    {
        move(-1);
    }
    
    public void moveRight()
    {
        move(1);
    }
    
    public void insert(String s)
    {
        contents = getSubstringLeft() + s + getSubstringRight();
        cursorPosition += s.length();
    }
    
    private String deleteLast(String s)
    {
        if (s.length() == 0)
            return s;
            
        return s.substring( 0, s.length() - 1 );
    }
    
    // removes char to the left of cursorPosition.
    public void delete()
    {
        if (cursorPosition == 0) 
            return;
        
        contents = deleteLast( getSubstringLeft() ) + getSubstringRight();
        cursorPosition--;
    }
    
    public void clear()
    {
        contents = "";
        cursorPosition = 0;
    }
    
    public String getSubstringLeft()
    {
        return contents.substring( 0, cursorPosition );
    }
    
    public String getSubstringRight()
    {
        return contents.substring( cursorPosition, contents.length() );
    }
    
    public String toString()
    {
        return getSubstringLeft() + "|" + getSubstringRight();
    }
}