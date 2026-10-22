package org.example;

public class Decorator {

    interface DataSource{
        void write_data();
    }

    public static class FileWriter implements DataSource{

        @Override
        public void write_data() {
            System.out.println("inside the file writer class");
        }
    }

    public static class FileWriter1 implements DataSource{
        DataSource wrapped;

        // Changed from private to public
        public FileWriter1(DataSource st)
        {
            this.wrapped = st;
        }

        @Override
        public void write_data() {
            System.out.println("inside the file writer class 1");
            this.wrapped.write_data();
        }
    }

    // Changed from static void main to public static void main
    public static void main(String[] args)
    {
        DataSource source = new FileWriter();
        source = new FileWriter1(source);
        source.write_data();
    }
}