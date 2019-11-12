local value = redis.call('sismember',KEYS[2], ARGV[1]);
if value == 1 then
    return 'GRAB_REPEAT';
else
    local hongBao = redis.call('rpop', KEYS[1]);
    if hongBao then
        local hongbaoJson = cjson.decode(hongBao);
        hongbaoJson['userId'] = ARGV[2];
        redis.call('zadd', KEYS[3], ARGV[3], cjson.encode(hongbaoJson));
        redis.call('sadd', KEYS[2], ARGV[1]);
        return 'GRAB_SUCCESS';
    else
        return 'GRAB_FAIL';
    end;
end;