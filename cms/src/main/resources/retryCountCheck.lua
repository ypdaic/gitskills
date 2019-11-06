local value = redis.call("get",KEYS[1]);
if value == false then
    redis.call("incr", KEYS[1]);
    redis.call("EXPIRE", KEYS[1], ARGV[1]);
    return true;
else
    local currentCount = tonumber(value);
    local checkCount = tonumber(ARGV[2]);
    if currentCount < checkCount then
        local expire = redis.call("ttl", KEYS[1]);
        redis.call("incr", KEYS[1]);
        redis.call("EXPIRE", KEYS[1], expire);
        return true;
    else
        return false;
    end;
end;