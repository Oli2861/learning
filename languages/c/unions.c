#include <stdio.h>
union intParts {
    int num;
    char bytes[sizeof(int)];
};

struct person {
    union {
        int intAge;
        float floatAge;
    } ageTypes;

    union {
        float floatMoney;
        double doubleMoney;
    };

    union {
        int coins;
        int münzen;
    };
};

int main(int argc, char *argv[])
{
    union intParts parts;
    parts.num = 300;
    printf("The int is %i\nThe bytes are [%i, %i, %i, %i]\n",
    parts.num, parts.bytes[0], parts.bytes[1], parts.bytes[2], parts.bytes[3]);

    int theInt = parts.num;
    printf("The int is %i\nThe bytes are [%i, %i, %i, %i]\n",
    theInt, *((char*)&theInt+0), *((char*)&theInt+1), *((char*)&theInt+2), *((char*)&theInt+3));

    // Struct with union
    struct person p;
    p.ageTypes.intAge = 10;
    printf("Age: %d\n", p.ageTypes.intAge);
    p.ageTypes.floatAge = 10.5;
    printf("Age: as int: %d as float: %f\n", p.ageTypes.intAge, p.ageTypes.floatAge);

    p.floatMoney = 12.5;
    printf("Balance: float: %f double %f\n", p.floatMoney, p.doubleMoney);
    p.doubleMoney = 11.5;
    printf("Balance: float: %f double %f\n", p.floatMoney, p.doubleMoney);

    p.coins = 11;
    printf("Cash: coins %d münzen %d\n", p.coins, p.münzen);
    p.münzen = 12;
    printf("Cash: coins %d münzen %d\n", p.coins, p.münzen);
    return 1;
}
