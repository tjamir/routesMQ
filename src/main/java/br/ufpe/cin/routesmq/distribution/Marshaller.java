package br.ufpe.cin.routesmq.distribution;


import java.io.*;

/**
 * Created by tjamir on 7/2/17.
 */
public class Marshaller {


    public byte[] marshall(Serializable serializable) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(baos);
        oos.writeObject(serializable);
        byte[] result=baos.toByteArray();
        oos.close();
        baos.close();
        return result;

    }


    public Object unMarshall(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais= new ByteArrayInputStream(data);
        ObjectInputStream ois=new ObjectInputStream(bais);
        Object object=ois.readObject();
        ois.close();
        bais.close();
        return object;
    }
}
