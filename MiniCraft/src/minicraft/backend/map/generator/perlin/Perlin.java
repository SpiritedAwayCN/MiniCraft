package minicraft.backend.map.generator.perlin;

public class Perlin {
    
    private double[][] perlinNoise;
    private double[][][] perlinGradients;
    private int h, w;
    private double biasX, biasZ;
    private boolean zeroBound;
    public Perlin(int h, int w, boolean zeroBound){
        this.h = h;
        this.w = w;
        this.zeroBound = zeroBound;
        perlinNoise = new double[h][w];
        perlinGradients = new double[h+1][w+1][2];
        // this.biasX = Math.random() * 15603;
        // this.biasZ = Math.random() * 11592;
        // generateGradients();
        generateMapByGradient();
        normalize();
    }

    private double fade(double t){
        return t*t*t*(t*(t*6-15)+10);
    }

    private double lerp(double t, double a, double b){
        return a + t * (b - a);
    }

    private class Vec2D{
        private double x, y;
        Vec2D(double x, double y){
            this.x = x;
            this.y = y;
        }
    }

    private Vec2D getRandomGradient(){
        int index = (int)(Math.random() * 8);
        double x = 2 * Math.PI * index / 8;
        double y = 2 * Math.PI * index / 8;
        return new Vec2D(Math.cos(x), Math.sin(y));
    }
    

    private void generateGradients(){
        for(int i = 0; i < h+1; i++)
            for(int j = 0; j < w+1; j++){
                if(zeroBound && (i<=1 || j <=1 || i>=h-1 || j>=w-1)){
                    perlinGradients[i][j][0] = 0;
                    perlinGradients[i][j][1] = 0;//边界零梯度
                    continue;
                }
                Vec2D grad = getRandomGradient();
                perlinGradients[i][j][0] = grad.x;
                perlinGradients[i][j][1] = grad.y;
            }
    }

    double minValue = 0, maxValue = 0;

    private void generateMapByGradient(){
        for(int i = 0; i < h; i++)
            for(int j = 0; j < w; j++){
                double ans = perlinNoiseNew((i - h*1.0/2)*0.04 + biasX, (j - w*1.0/2)*0.04 + biasZ);
                if(ans < minValue) minValue = ans;
                if(ans > maxValue) maxValue = ans;
                perlinNoise[i][j] = ans;
                // perlinNoise[i][j] = produce(i / 32, j / 32, (i % 32)*1.0/32+1.0/64, (j % 32)*1.0/32+1.0/64);
            }
    }

    private double produce(int i, int j, double dx, double dy){
        double v00 = dot2D(perlinGradients[i][j][0],  perlinGradients[i][j][1], dx, dy);
        double v01 = dot2D(perlinGradients[i][j + 1][0],  perlinGradients[i][j + 1][1], dx, dy - 1);
        double v10 = dot2D(perlinGradients[i + 1][j][0],  perlinGradients[i + 1][j][1], dx - 1, dy);
        double v11 = dot2D(perlinGradients[i + 1][j + 1][0],  perlinGradients[i + 1][j + 1][1], dx - 1, dy - 1);

        double temp = fade(dx);
        double lpx0 = lerp(temp, v00, v10), lpx1 = lerp(temp, v01, v11);
        double ans = lerp(lpx0, lpx1, fade(dy));

        if(ans < minValue) minValue = ans;
        if(ans > maxValue) maxValue = ans;

        return ans;
    }

    private double dot2D(double x1, double y1, double x2, double y2){
        return x1*x2+y1*y2;
    }

    private void normalize(){
        for(int i = 0; i < h; i++)
            for(int j = 0; j < w; j++){
                perlinNoise[i][j] = (perlinNoise[i][j] - minValue) / (maxValue - minValue);
            }
    }

    public double[][] getPerlinNoise() {
        return perlinNoise;
    }

    private double persistence1 = 0.001;
    private int Number_of_Octaves1 = 2;
    private double noise(int x, int y){
        int n=x+y*59;
        n = (n >> 13) ^ n;
        int nn = (n * (n * n * 15731 + 789211) + 700000001) & 0x7fffffff;
        return 1.0 - (nn / 1073741824.0);
    }
    private double smoothedNoise(int x, int y){
        double corners = ( noise(x-1, y-1)+noise(x+1, y-1)+noise(x-1, y+1)+noise(x+1, y+1) ) / 16;
        double sides = ( noise(x-1, y) +noise(x+1, y) +noise(x, y-1) +noise(x, y+1) ) / 8;
        double center = noise(x, y) / 4;
        return corners + sides + center;
    }
    private double cosineInterpolate(double a, double b, double x)  // 余弦插值
    {
        double ft = x * Math.PI;
        double f = (1 - Math.cos(ft)) * 0.5;
        return a*(1-f) + b*f;
    }
    private double interpolatedNoise(double x, double y)   // 获取插值噪声
    {
        int integer_X = (int)Math.floor(x);
        double fractional_X = x - integer_X;
        int integer_Y = (int)Math.floor(y);
        double fractional_Y = y - integer_Y;
        double v1 = smoothedNoise(integer_X, integer_Y);
        double v2 = smoothedNoise(integer_X + 1, integer_Y);
        double v3 = smoothedNoise(integer_X, integer_Y + 1);
        double v4 = smoothedNoise(integer_X + 1, integer_Y + 1);
        double i1 = cosineInterpolate(v1, v2, fractional_X);
        double i2 = cosineInterpolate(v3, v4, fractional_X);
        return cosineInterpolate(i1, i2, fractional_Y);
    }
    private double perlinNoiseNew(double x, double y)    // 最终调用：根据(x,y)获得其对应的PerlinNoise值
    {
        double total = 0;
        double p = persistence1;
        int n = Number_of_Octaves1;
        for(int i=0; i<n; i++)
        {
            double frequency = Math.pow(2,i);
            double amplitude = Math.pow(p,i);
            total = total + interpolatedNoise(x * frequency, y * frequency) * amplitude;
        }
     
        return total;
    }
}