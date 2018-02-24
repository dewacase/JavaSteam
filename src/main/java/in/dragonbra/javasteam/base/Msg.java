package in.dragonbra.javasteam.base;

import in.dragonbra.javasteam.enums.EMsg;
import in.dragonbra.javasteam.generated.MsgHdr;
import in.dragonbra.javasteam.types.JobID;
import in.dragonbra.javasteam.types.SteamID;
import in.dragonbra.javasteam.util.stream.BinaryWriter;
import in.dragonbra.javasteam.util.stream.MemoryStream;
import in.dragonbra.javasteam.util.stream.SeekOrigin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Represents a struct backed message without session or client info.
 */
public class Msg<BodyType extends ISteamSerializableMessage> extends MsgBase<MsgHdr> {

    private static final Logger logger = LogManager.getLogger(Msg.class);

    private BodyType body;

    /**
     * Initializes a new instance of the {@link Msg} class.
     *
     * @param bodyType body type
     */
    public Msg(Class<? extends BodyType> bodyType) {
        this(bodyType, 0);
    }

    /**
     * Initializes a new instance of the {@link Msg} class.
     *
     * @param bodyType       body type
     * @param payloadReserve The number of bytes to initialize the payload capacity to.
     */
    public Msg(Class<? extends BodyType> bodyType, int payloadReserve) {
        super(MsgHdr.class, payloadReserve);

        try {
            body = bodyType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.debug(e);
        }

        getHeader().setEMsg(body.getEMsg());
    }

    /**
     * Initializes a new instance of the {@link Msg} class.
     * This a reply constructor.
     *
     * @param bodyType body type
     * @param msg      The message that this instance is a reply for.
     */
    public Msg(Class<? extends BodyType> bodyType, MsgBase<MsgHdr> msg) {
        this(bodyType, msg, 0);
    }

    /**
     * Initializes a new instance of the {@link Msg} class.
     * This a reply constructor.
     *
     * @param bodyType       body type
     * @param msg            The message that this instance is a reply for.
     * @param payloadReserve The number of bytes to initialize the payload capacity to.
     */
    public Msg(Class<? extends BodyType> bodyType, MsgBase<MsgHdr> msg, int payloadReserve) {
        this(bodyType, payloadReserve);

        if (msg == null) {
            throw new IllegalArgumentException("msg is null");
        }

        // our target is where the message came from
        getHeader().setTargetJobID(msg.getHeader().getSourceJobID());
    }

    /**
     * Initializes a new instance of the {@link Msg} class.
     * This a receive constructor.
     *
     * @param bodyType body type
     * @param msg      The packet message to build this client message from.
     */
    public Msg(Class<? extends BodyType> bodyType, IPacketMsg msg) {
        this(bodyType);

        if (msg == null) {
            throw new IllegalArgumentException("msg is null");
        }

        try {
            deserialize(msg.getData());
        } catch (IOException e) {
            logger.debug(e);
        }
    }

    @Override
    public boolean isProto() {
        return false;
    }

    @Override
    public EMsg getMsgType() {
        return getHeader().getMsg();
    }

    @Override
    public int getSessionID() {
        return 0;
    }

    @Override
    public void setSessionID(int sessionID) {
    }

    @Override
    public SteamID getSteamID() {
        return null;
    }

    @Override
    public void setSteamID(SteamID steamID) {
    }

    @Override
    public JobID getTargetJobID() {
        return new JobID(getHeader().getTargetJobID());
    }

    @Override
    public void setTargetJobID(JobID jobID) {
        if (jobID == null) {
            throw new IllegalArgumentException("jobID is null");
        }
        getHeader().setTargetJobID(jobID.getValue());
    }

    @Override
    public JobID getSourceJobID() {
        return new JobID(getHeader().getSourceJobID());
    }

    @Override
    public void setSourceJobID(JobID jobID) {
        if (jobID == null) {
            throw new IllegalArgumentException("jobID is null");
        }
        getHeader().setSourceJobID(jobID.getValue());
    }

    @Override
    public byte[] serialize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(0);
        BinaryWriter dos = new BinaryWriter(baos);

        getHeader().serialize(baos);
        body.serialize(baos);
        dos.write(payload.toByteArray());
        return baos.toByteArray();
    }

    @Override
    public void deserialize(byte[] data) throws IOException {
        if (data == null) {
            throw new IllegalArgumentException("data is null");
        }
        MemoryStream ms = new MemoryStream(data);

        getHeader().deserialize(ms);
        getBody().deserialize(ms);

        payload.write(data, (int) ms.getPosition(), ms.available());
        payload.seek(0, SeekOrigin.BEGIN);
    }

    public BodyType getBody() {
        return body;
    }
}