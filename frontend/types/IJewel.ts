export interface IJewel {
    jewelId: bigint,
    name: string,
    title: string,
    description: string,
    imagepaths: string[],
    color: string,
    karat: string,
    type: string,
    endDate: Date,
    published: boolean,
    auction: number
}