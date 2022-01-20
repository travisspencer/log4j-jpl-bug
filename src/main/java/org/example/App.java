package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

public final class App
{
    private static final Logger _logger = LogManager.getLogger(App.class);

    public static void main(String[] args) throws Exception
    {
        _logger.info("Hello World!");

        // Bug is not shown without this
        ObjectInputFilter.Config.setSerialFilter(filterInfo -> ObjectInputFilter.Status.UNDECIDED);

        SerializedData object1 = new SerializedData(42);

        File tempFile = File.createTempFile("tempfile", ".tmp");
        tempFile.deleteOnExit();

        serialize(tempFile, object1);

        SerializedData object2 = deserialize(tempFile);

        boolean objectsEqual = Objects.equals(object1, object2);

        _logger.debug("object1 equals object2? {}", objectsEqual);

        assert objectsEqual : "Objects not equal";
    }

    private static void serialize(File fileName, Object object) throws IOException
    {
        try (FileOutputStream file = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(file))
        {
            out.writeObject(object);
        }

        _logger.debug("Object has been serialized");
    }

    private static SerializedData deserialize(File fileName)
            throws IOException, ClassNotFoundException
    {
        try (FileInputStream file = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(file))
        {
            SerializedData object = (SerializedData)in.readObject();

            _logger.debug("Object has been deserialized ");

            return object;
        }
    }

    private static final class SerializedData implements Serializable
    {
        private final int _x;

        public SerializedData(int x)
        {
            _x = x;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }

            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            SerializedData that = (SerializedData) o;

            return _x == that._x;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(_x);
        }
    }
}
