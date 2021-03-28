#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void print(char* str) {
    printf("%s", str);
}

void println(char* str) {
    printf("%s\n", str);
}

void printInt(int n) {
    printf("%d", n);
}

void printlnInt(int n) {
    printf("%d\n", n);
}

char* getString() {
    char* buffer = (char*)malloc(sizeof(char) * 257);
    scanf("%s", buffer);
    return buffer;
}

int getInt() {
    int n;
    scanf("%d", &n);
    return n;
}

char _string_less(char* str1, char* str2) {
    return strcmp(str1, str2) < 0;
}

char _string_greater(char* str1, char* str2) {
    return strcmp(str1, str2) > 0;
}

char _string_lessEqual(char* str1, char* str2) {
    return strcmp(str1, str2) <= 0;
}

char _string_greaterEqual(char* str1, char* str2) {
    return strcmp(str1, str2) >= 0;
}

char _string_equal(char* str1, char* str2) {
    return strcmp(str1, str2) == 0;
}

char _string_notEqual(char* str1, char* str2) {
    return strcmp(str1, str2) != 0;
}

int _string_length (char *string){
    return strlen(string);
}

char* toString(int i) {
    if (i == 0) {
        char* res = (char*)malloc(sizeof(char) * 2);
        res[0] = '0';
        res[1] = '\0';
        return res;
    }

    char tmp[10];
    char neg, len = 0;
    if (i > 0) {
        neg = 0;
    } else {
        neg = 1;
        i = -i;
    }
    while (i > 0) {
        tmp[len++] = i % 10;
        i /= 10;
    }

    char* res = (char*) malloc(sizeof(char) * (neg + len + 1));
    if (neg > 0)
        res[0] = '-';
    for (char p = 0; p < len; p++)
        res[p + neg] = tmp[len - p - 1] + '0';
    res[len + neg] = '\0';

    return res;
}

char* _string_substring(char* str, int left, int right) {
    int len = right - left;
    char* res = (char*) malloc(sizeof(char) * (len + 1));

    for (char p = 0; p < len; p++)
        res[p] = str[left + p];

    res[len] = '\0';

    return res;
}

int _string_ord(char* str, int p) {
    return str[p];
}

char* _string_concatenate(char* str1, char* str2) {
    int len1 = strlen(str1), len2 = strlen(str2);

    char* res = (char*) malloc(sizeof(char) * (len1 + len2 + 1));
    int len = 0, p = 0;
    while (p < len1) res[len++] = str1[p++];
    p = 0;
    while (p < len2) res[len++] = str2[p++];

    res[len] = '\0';

    return res;
}

int _string_parseInt(char* str) {
    int res = 0, p = 0;
    while (str[p] != '\0' && str[p] >= '0' && str[p] <= '9')
        res = res * 10 + str[p++] - '0';
    return res;
}

int _array_size(char* array) {
    return *(((int*) array) - 1);
}