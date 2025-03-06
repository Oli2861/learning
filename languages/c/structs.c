typedef struct {
    int x;
    int y;
} coordinates;

struct position {
    int x;
    int y;
};

void moveRight(struct position *p) {
    (*p).x++;
    p->x--;
    p->x++;
}

int main(int argc, char *argv[])
{
    struct position pos;
    struct position *p = &pos;
    (*p).x = 10;
    (*p).y = 10;
    moveRight(p);

    coordinates c;
    c.x = 10;
    c.y = 10;

    return 0;
}
