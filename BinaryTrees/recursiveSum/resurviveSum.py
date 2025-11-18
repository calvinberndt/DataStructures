def resursiveSum(array):
    if len(array) == 0:
        return 0
    return array[0] + resursiveSum(array[1:])

print(resursiveSum([1, 2, 3, 4, 5]))