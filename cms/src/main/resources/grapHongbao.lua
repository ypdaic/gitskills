local value = redis.call("SISMEMBER",KEYS[2], ARGV[1]);
if value == 1 then
    return false;
else
    local hongbao = redis.call("rpop", KEYS[2]);
    local hongbaoJson = cjson.decode(hongbao);
    hongbaoJson['userID'] = ARGV[2];

    redis.call("zsadd", KEY[3], ARGV[3], cjson.encode(hongbaoJson));
    redis.call("sadd", KEY[2], ARGV[1]);
    return true;
end;