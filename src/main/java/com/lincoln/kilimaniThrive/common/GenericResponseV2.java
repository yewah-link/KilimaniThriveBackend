package com.lincoln.kilimaniThrive.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponseV2<T> {
    private ResponseStatusEnum status;
    private String message;
    
    @JsonProperty("_embedded")
    private T _embedded;

    public ResponseStatusEnum getStatus() { return status; }
    public void setStatus(ResponseStatusEnum status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    @JsonProperty("_embedded")
    public T get_embedded() { return _embedded; }
    
    @JsonProperty("_embedded")
    public void set_embedded(T _embedded) { this._embedded = _embedded; }
}
