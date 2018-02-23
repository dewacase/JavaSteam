package in.dragonbra.javasteam.generated;

import in.dragonbra.javasteam.base.IGCSerializableHeader;
import in.dragonbra.javasteam.protobufs.steamclient.SteammessagesBase.CMsgProtoBufHeader;
import in.dragonbra.javasteam.util.stream.BinaryReader;

import java.io.*;

public class MsgGCHdrProtoBuf implements IGCSerializableHeader {

    private long msg = 0L;

    private int headerLength = 0;

    private CMsgProtoBufHeader.Builder proto = CMsgProtoBufHeader.newBuilder();

    @Override
    public void setEMsg(int msg) {
        this.msg = msg;
    }

    public long getMsg() {
        return this.msg;
    }

    public void setMsg(long msg) {
        this.msg = msg;
    }

    public int getHeaderLength() {
        return this.headerLength;
    }

    public void setHeaderLength(int headerLength) {
        this.headerLength = headerLength;
    }

    public CMsgProtoBufHeader.Builder getProto() {
        return this.proto;
    }

    public void setProto(CMsgProtoBufHeader.Builder proto) {
        this.proto = proto;
    }

    @Override
    public void serialize(OutputStream stream) throws IOException {
        DataOutputStream dos = new DataOutputStream(stream);

        dos.writeLong(msg);
        dos.writeInt(headerLength);
        byte[] protoBuffer = proto.build().toByteArray();
        dos.writeInt(protoBuffer.length);
        dos.write(protoBuffer);
    }

    @Override
    public void deserialize(InputStream stream) throws IOException {
        BinaryReader br = new BinaryReader(stream);

        msg = br.readLong();
        headerLength = br.readInt();
        byte[] protoBuffer = br.readBytes(br.readInt());
        proto = CMsgProtoBufHeader.newBuilder().mergeFrom(protoBuffer);
    }
}