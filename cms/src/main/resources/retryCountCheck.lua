local value = redis.call("get",KEYS[1]);
if value == nil then
    redis.call("incr");
    redis.call("EXPIRE", ARGV[1]);
    return 1;
else
    if value < 10 then
        redis.call("incr");
        redis.call("EXPIRE", ARGV[1]);
        return 1;
    else
        return 0
    end;
end;