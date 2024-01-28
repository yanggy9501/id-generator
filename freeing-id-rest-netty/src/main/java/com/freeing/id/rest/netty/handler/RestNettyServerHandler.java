package com.freeing.id.rest.netty.handler;

import com.alibaba.fastjson.JSON;
import com.freeing.id.core.bean.Id;
import com.freeing.id.manager.IdManager;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@Sharable
public class RestNettyServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RestNettyServerHandler.class);

	/************************ request action params *************************/
	private static final String ID = "id";
    private static final String VERSION = "version";
    private static final String TYPE = "type";
    private static final String GENMETHOD = "genMethod";
    private static final String MACHINE = "machine";
    private static final String TIME = "time";
    private static final String SEQ = "seq";

    /************************ request action url ***************************/
    private static final String ACTION_GENID = "/genid";
    private static final String ACTION_EXPID = "/expid";
    private static final String ACTION_TRANSTIME = "/transtime";
    private static final String ACTION_MAKEID = "/makeid";


    private IdManager idManager;

    public RestNettyServerHandler(IdManager idManager) {
        this.idManager = idManager;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof HttpRequest)) {
            return;
        }

        HttpRequest req = (HttpRequest) msg;

        if (is100ContinueExpected(req)) {
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }

        URI uri = new URI(req.uri());
		logger.debug("request uri {}", uri.getPath());

        long id = -1;
        long time = -1;
        long version = -1;
        long type = -1;
        long genmethod = -1;
        long machine = -1;
        long seq = -1;

        QueryStringDecoder decoderQuery = new QueryStringDecoder(req.uri());
        Map<String, List<String>> uriAttributes = decoderQuery.parameters();

        for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
            for (String attrVal : attr.getValue()) {
                logger.debug("Request Parameter: {}={}", attr.getKey(), attrVal);

                // 获取请求参数
                if (ID.equals(attr.getKey())) {
                    id = Long.parseLong(attrVal);
                } else if (TIME.equals(attr.getKey())) {
                    time = Long.parseLong(attrVal);
                } else if (VERSION.equals(attr.getKey())) {
                    version = Long.parseLong(attrVal);
                } else if (TYPE.equals(attr.getKey())) {
                    type = Long.parseLong(attrVal);
                } else if (GENMETHOD.equals(attr.getKey())) {
                    genmethod = Long.parseLong(attrVal);
                } else if (MACHINE.equals(attr.getKey())) {
                    machine = Long.parseLong(attrVal);
                } else if (SEQ.equals(attr.getKey())) {
                    seq = Long.parseLong(attrVal);
                }
            }
        }

        StringBuilder sbContent = new StringBuilder();

        if (ACTION_GENID.equals(uri.getPath())) {
            long idl = idManager.genId();
            logger.trace("Generated id: {}", idl);
            sbContent.append(idl);
        } else if (ACTION_EXPID.equals(uri.getPath())) {
            Id ido = idManager.expId(id);
            logger.trace("Explained id: {}", ido);
            String jo = JSON.toJSONString(ido);
            sbContent.append(jo);
        } else if (ACTION_TRANSTIME.equals(uri.getPath())) {
            Date date = idManager.transTime(time);
            logger.trace("Time: {} ", date);
            sbContent.append(date);
        } else if (ACTION_MAKEID.equals(uri.getPath())) {
            long madeId = -1;
            if (time == -1 || seq == -1) {
                sbContent.append("Both time and seq are required.");
            } else if (version == -1) {
                if (type == -1) {
                    if (genmethod == -1) {
                        madeId = machine == -1 ? idManager.makeId(time, seq) : idManager.makeId(machine, time, seq);
                    } else {
                        madeId = idManager.makeId(genmethod, machine, time, seq);
                    }
                } else {
                    madeId = idManager.makeId(type, genmethod, machine, time, seq);
                }
            } else {
                madeId = idManager.makeId(version, type, genmethod, machine, time, seq);
            }

			logger.trace("Id: {}", madeId);

            sbContent.append(madeId);
        } else {
            sbContent.append("\r\n");
            sbContent.append("Please input right URI:");
            sbContent.append("\r\n");
            sbContent.append("    Example 1: http://ip:port/genid");
            sbContent.append("\r\n");
            sbContent.append("    Example 2: http://ip:port/expid?id=?");
            sbContent.append("\r\n");
            sbContent.append("    Example 3: http://ip:port/transtime?time=?");
            sbContent.append("\r\n");
            sbContent.append("    Example 4: http://ip:port/makeid?version=?&type=?&genmethod=?&machine=?&time=?&seq=?");
        }

		logger.trace("Message body: {}", sbContent);

        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
            Unpooled.wrappedBuffer(sbContent.toString().getBytes(StandardCharsets.UTF_8)));

        response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

        boolean keepAlive = isKeepAlive(req);

            logger.trace("Keep Alive: {}", keepAlive);

        if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            ctx.write(response);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("HTTP Server Error", cause);
        ctx.close();
    }
}
