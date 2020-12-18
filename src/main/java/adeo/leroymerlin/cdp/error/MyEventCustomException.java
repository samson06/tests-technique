package adeo.leroymerlin.cdp.error;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.http.HttpStatus;

/**
 * Object for handling errors or exceptions caught.
 * 
 * @author Vincent Otchoun
 */
public class MyEventCustomException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 5656840536997387213L;

    //
    private HttpStatus status;
    private LocalDateTime timestamp;
    private String errorCode;
    private String message;

    /**
     * Default constructor.
     */
    public MyEventCustomException()
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * Constructor with status message and error code.
     * 
     * @param status    error http status code.
     * @param errorCode error code.
     * @param mesaage   error message.
     */
    public MyEventCustomException(HttpStatus status, String errorCode, String message)
    {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = LocalDateTime.now(ZoneId.systemDefault());
    }

    public HttpStatus getStatus()
    {
        return this.status;
    }

    public LocalDateTime getTimestamp()
    {
        return this.timestamp;
    }

    public String getErrorCode()
    {
        return this.errorCode;
    }

    @Override
    public String getMessage()
    {
        return this.message;
    }


    public void setStatus(HttpStatus status)
    {
        this.status = status;
    }

    public void setTimestamp(LocalDateTime timestamp)
    {
        this.timestamp = timestamp;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
