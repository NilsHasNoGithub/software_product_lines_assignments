## Server-client communication protocol
1. Client sends encrypted password
2. Client sends encrypted json body, which can be any of:
```json
{
    "type": "sendMsg",
    "message": {
        "content": "<<content>>",
        "username": "<<username>>",
        "color": [0, 0, 0]
    }
}
```
```json
{
    "type": "requestMsgs"
}
```
```json
{
    "type": "stop"
}
```
3. Server responds with the updated list of messages, or stops (in the last case)
```json
[
    {
        "content": "<<content>>",
        "username": "<<username>>",
        "color": [0, 0, 0]
    },
    {
        "content": "<<content>>",
        "username": "<<username>>",
        "color": [0, 0, 0]
    },
]
```

## Compile options
```
REVERSER_ENCRYPTION: boolean
```