#This is a quick example on how to use conditionals such as "then" and "else" in a shell script.

echo "Enter a number: "
read num

echo "Enter another number: "
read num2

if [ $num -gt $num2 ]
then
    echo "$num is greater than $num2"
else
    echo "$num2 is greater than $num"
fi

