package in.dragonbra.javasteam.generated;

import in.dragonbra.javasteam.base.ISteamSerializableMessage;
import in.dragonbra.javasteam.enums.EMsg;
import in.dragonbra.javasteam.util.stream.BinaryReader;

import java.io.*;

public class MsgClientOGSWriteRow implements ISteamSerializableMessage {

    private long sessionId = 0L;

    private int countAttributes = 0;

    @Override
    public EMsg getEMsg() {
        return EMsg.ClientOGSWriteRow;
    }

    public long getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public int getCountAttributes() {
        return this.countAttributes;
    }

    public void setCountAttributes(int countAttributes) {
        this.countAttributes = countAttributes;
    }

    @Override
    public void serialize(OutputStream stream) throws IOException {
        DataOutputStream dos = new DataOutputStream(stream);

        dos.writeLong(sessionId);
        dos.writeInt(countAttributes);
    }

    @Override
    public void deserialize(InputStream stream) throws IOException {
        BinaryReader br = new BinaryReader(stream);

        sessionId = br.readLong();
        countAttributes = br.readInt();
    }
}